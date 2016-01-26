(ns hackernews.api
  (:require [re-frame.core :as re-frame]
            [matchbox.core :as m]))

(def api-url "https://hacker-news.firebaseio.com")

(def root (m/connect api-url))

(m/auth-anon root)

(def API-VERSION :v0)

(def sids (atom []))

;(def set-stories [story-ids]
;  (set! sids story-ids))

;(defn get-story-ids []
;  (m/deref-in root [API-VERSION :topstories] #(set! sids %))
;  sids) 
(defn get-story-ids []
  (m/deref-in root [API-VERSION :topstories] #(re-frame/dispatch :process-stories)))

(defn get- [{:keys [endpoint cb] :as d}]
  (prn "calling get..")
  (m/deref-in root [API-VERSION endpoint] cb))

;--  firebase
; https://github.com/crisptrutski/matchbox
;-- hacknews api
; https://github.com/HackerNews/API

; listen to top story id's..
;(m/listen-children root [:v0 :topstories])
