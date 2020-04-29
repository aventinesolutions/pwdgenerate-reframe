(ns pwdgenerator-reframe.firebase
  (:require
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.subs :as subs]
    [pwdgenerator-reframe.events :as events]
    [com.degel.re-frame-firebase :as firebase]))

(defonce firebase-app-info {})

(re-frame/reg-event-fx
  ::firebase-error
  (fn [_ [_ error]]
    (.log js/console error)))

(defonce firebase-instance (atom nil))

(defn ^:export init []
  (when-not @firebase-instance
    (reset! firebase-instance
            (firebase/init :firebase-app-info firebase-app-info
                           :firestore-settings {}
                           :get-user-sub [::subs/user]
                           :set-user-event [::events/user]
                           :default-error-handler [::firebase-error]))))

(re-frame/reg-event-fx
  ::sign-in-by-email
  (fn [_ [_ [email password]]]
    {:firebase/email-sign-in {:email email :password password}}))

(re-frame/reg-event-fx
  ::sign-out
  (fn [_ _]
    {:firebase/sign-out nil}))
