(ns pwdgenerator-reframe.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [pwdgenerator-reframe.events :as events]
   [pwdgenerator-reframe.routes :as routes]
   [pwdgenerator-reframe.views :as views]
   [pwdgenerator-reframe.config :as config]))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn init []
  (routes/app-routes)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
