(ns hackernews.views
    (:require [re-frame.core :as re-frame]
              [re-com.core :as re-com]))


;; home

(defn home-title []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [re-com/title
       :label (str "Hello from " @name ". This is the Home Page.")
       :level :level1])))

(defn home-label []
  [re-com/label
   :label "This is just a test"
   :on-click #(re-frame/dispatch [:click-event])])

(defn story-summary [story-id]
  [:li 
   ^{:key (str "_" story-id)} [re-com/hyperlink-href
    :label (str "Click for story: " story-id)
    :href (str "#/story/" story-id)
   ]])
;   :on-click #(re-frame/dispatch [:view-story story-id])]])
;

(defn load-stories []
  [:li 
   [re-com/label
    :label "Click to load stories!"
    :on-click #(re-frame/dispatch [:load-stories])]])

(defn stories []
  [:ul 
   (load-stories)
   ])
   ;(for [story-id (range 10)]
   ;  ^{:key story-id} (story-summary story-id))])


(defn link-to-about-page []
  [re-com/hyperlink-href
   :label "go to About Page"
   :href "#/about"])

(defn home-panel []
  [re-com/v-box
   :gap "1em"
   :children [
              [home-title] 
              [home-label]
              [stories]
              [link-to-about-page]]])

;; story 
(defn story-title []
  [re-com/title
   :label "This is the story view"
   :level :level1])

(defn story-body []
  [re-com/label
   :label "This is the story body"])

(defn story-panel []
  [re-com/v-box
   :gap "1em"
   :children [[story-title] [story-body]]])

;; about

(defn about-title []
  [re-com/title
   :label "This is the About Page."
   :level :level1])

(defn link-to-home-page []
  [re-com/hyperlink-href
   :label "go to Home Page"
   :href "#/"])

(defn about-panel []
  [re-com/v-box
   :gap "1em"
   :children [[about-title] [link-to-home-page]]])


;; main

(defmulti panels identity)
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :story-panel [] [story-panel])
(defmethod panels :default [] [:div])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [re-com/v-box
       :height "100%"
       :children [(panels @active-panel)]])))
