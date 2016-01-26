(ns hackernews.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [hackernews.handlers]
              [hackernews.subs]
              [hackernews.routes :as routes]
              [hackernews.views :as views]
              [hackernews.config :as config]))

(when config/debug?
  (println "dev mode"))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init [] 
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  ;(re-frame/dispatch-sync [:load-stories])
  (mount-root))
