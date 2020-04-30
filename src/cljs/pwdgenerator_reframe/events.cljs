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

;(if (not (nil? user))
;  (re-frame/dispatch-sync [::get-initial-personalized]))

(re-frame/reg-event-fx
  ::sign-in-by-email
  (fn [_ [_ [email password]]]
    {:firebase/email-sign-in {:email email :password password}}))

(re-frame/reg-event-fx
  ::sign-out
  (fn [_ _]
    {:firebase/sign-out nil}))

(re-frame/reg-event-db
  ::update-personalized
  (fn [db [_ personalized]]
    (do
      (assoc db :personalized personalized)
      (if (empty? personalized) (re-frame/dispatch-sync [::set-initial-personalized])))))

(re-frame/reg-event-fx
  ::get-initial-personalized
  (fn [_ _]
    {:firestore/get {:path-document [:params]
                     :on-success    [::update-personalized]}}))

(re-frame/reg-event-fx
  ::set-initial-personalized
  (fn [_ _]
    {:firestore/set {:path-document [:params]
                     :data          [{:name "default" :params db/defaults}]
                     :on-success    [::update-personalized]}}))
