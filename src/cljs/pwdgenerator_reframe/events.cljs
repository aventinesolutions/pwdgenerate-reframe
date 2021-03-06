(ns pwdgenerator-reframe.events
  (:require
    [cljs.pprint :refer [pprint]]
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.db :as db :refer [defaults]]
    [pwdgenerator-reframe.domain :refer [generate-pw]]
    [com.degel.re-frame-firebase :as firebase]))

(re-frame/reg-event-db
  ::initialize-db
  (fn [_ _]
    db/default-db))

(re-frame/reg-event-db
  ::set-active-panel
  (fn [db [_ active-panel]]
    (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
  ::params
  (fn [db [_ params]]
    (assoc db :params params :value (generate-pw params))))

(re-frame/reg-event-db
  ::generate
  (fn [db [_ _]]
    (assoc db :value (generate-pw (:params db)))))

(re-frame/reg-event-db
  ::reset
  (fn [db [_ _]]
    (assoc db :value (generate-pw defaults) :params defaults)))

(re-frame/reg-event-db
  ::value
  (fn [db [_ value]]
    (assoc db :value value)))

(re-frame/reg-event-db
  ::show?
  (fn [db [_ show?]]
    (assoc db :show? show?)))

(re-frame/reg-event-db
  ::user
  (fn [db [_ user]]
    (assoc db :user user)))

(re-frame/reg-event-fx
  ::sign-in-by-email
  (fn [_ [_ [email password]]]
    {:firebase/email-sign-in {:email email :password password}}))

(re-frame/reg-event-fx
  ::sign-out
  (fn [_ _]
    {:firebase/sign-out nil}))
