(ns hackernews.handlers
    (:require [re-frame.core :as re-frame]
              [hackernews.db :as db]
              [hackernews.api :as api]
              [matchbox.core :as matchbox]))

(re-frame/register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/register-handler
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

;(re-frame/register-handler
; :initialize-stories
; (fn [db _]
;   (let [result (matchbox/deref api/root [:v0 :topstories])]
;     (println result))
;   db))


(re-frame/register-handler
  :load-stories
  (fn [db _]
    (prn "loading stories")
    (prn "got here..")
    (api/get- {:endpoint :topstories
               :cb #(re-frame/dispatch [:process-stories %])})
    db))

(re-frame/register-handler
 :process-stories
 (fn [db [_ story-ids]]
   (prn "processing stories")
   (prn story-ids)
   (re-frame/dispatch [:populate-stories])
   (if (empty? story-ids)
     db
     (assoc db :story-ids story-ids))))

(re-frame/register-handler
  :populate-stories
  (fn [db e]
    (prn e)
    (prn "populating stories...")
    (let [story-ids (take 10 (get db :story-ids))]
      (prn story-ids)
      (for [story-id (take 10 story-ids)]
        (prn (str "here is : " story-id))
        ))
        ;(re-frame/dispatch [:load-story story-id])))
    db))

(re-frame/register-handler
  :load-story
  (fn [db story-id]
    (prn "loading story")
    (if-not (contains? db story-id)
      ; only update stories we haven't seen before
      (api/get- {:endpoints [:item (str story-id ".json")]
                 :cb #(re-frame/dispatch [:process-story %])}))
    db))

(re-frame/register-handler
  :process-story
  (fn [db {:keys [id] :as story}]
    (prn "processing story")
    (prn (str "id: " id " - " story)) 
    (assoc db id story)))

(re-frame/register-handler
 :process-failure
 (fn [db event]
   (prn event)
   (when-let [spinner (get-in event :spinner)]
     (dissoc db spinner))))
