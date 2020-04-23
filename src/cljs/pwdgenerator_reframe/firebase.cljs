(ns pwdgenerator-reframe.firebase
  (:require
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.subs :as subs]
    [pwdgenerator-reframe.events :as events]
    [com.degel.re-frame-firebase :as firebase]))

(defonce firebase-app-info {:apiKey            "AIzaSyDBJGdi-3rLcUO5SgA4-dwPqWIH1IeysZo",
                            :authDomain        "pwdgenerator-reframe.firebaseapp.com",
                            :databaseURL       "https://pwdgenerator-reframe.firebaseio.com",
                            :projectId         "pwdgenerator-reframe",
                            :storageBucket     "pwdgenerator-reframe.appspot.com",
                            :messagingSenderId "266579826485",
                            :appId             "1:266579826485:web:90964aa050285094431e7c",
                            :measurementId     "G-T8N6XH2956"})

(defonce firebase-instance (atom nil))

(defn ^:export init []
  (when-not @firebase-instance
    (reset! firebase-instance
            (firebase/init :firebase-app-info firebase-app-info
                           :firestore-settings {}
                           :get-user-sub [::subs/user]
                           :set-user-event [::events/user]
                           :default-error-handler [:firebase-error]))))

(re-frame/reg-event-fx
  ::sign-in-by-email
  (fn [_ [_ [email password]]]
    {:firebase/email-sign-in {:email email :password password}}))

(re-frame/reg-event-fx
  ::sign-out
  (fn [_ _]
    {:firebase/sign-out nil}))




