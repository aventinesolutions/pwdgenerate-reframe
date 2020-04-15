(ns pwdgenerator-reframe.events
  (:require
    [cljs.pprint :refer [pprint]]
    [re-frame.core :as re-frame]
    [firebase.core :refer [init]]
    ["firebase" :as Firebase]
    [pwdgenerator-reframe.db :as db :refer [defaults]]
    [pwdgenerator-reframe.domain :refer [generate-pw]]))

(defn email-sign-in [{:keys [:email :password]}]
  (-> Firebase
      .auth
      (.signInWithEmailAndPassword email password)))

(defn current-user []
  (-> Firebase .auth .-currentUser))

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
  ::firebase-login
  (fn [db [_ credentials]]
    (do
      (init {})
      (email-sign-in credentials)
      (pprint (current-user))
      (assoc db :user (current-user)))))
