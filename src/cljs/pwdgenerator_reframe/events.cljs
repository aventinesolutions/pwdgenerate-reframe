(ns pwdgenerator-reframe.events
  (:require
   [re-frame.core :as re-frame]
   [pwdgenerator-reframe.db :as db]
   [pwdgenerator-reframe.domain :refer [generate-pw]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
  :generate
  (fn [db [_ _]]
    (assoc db :value (generate-pw (:params db)))))
