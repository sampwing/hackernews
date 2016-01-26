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
 (fn [db story-ids]
   (prn "processing stories")
   (prn story-ids)
   (if (empty? story-ids)
     db
     (assoc db :stories story-ids))))

(re-frame/register-handler
 :process-failure
 (fn [db event]
   (prn event)
   (when-let [spinner (get-in event :spinner)]
     (dissoc db spinner))))
