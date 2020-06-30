(ns pwdgenerator-reframe.firebase
  (:require
    [cljs.pprint :refer [pprint]]
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.subs :as subs]
    [pwdgenerator-reframe.events :as events]
    [com.degel.re-frame-firebase :as firebase]))

(goog-define API_KEY "<secret>")
(goog-define AUTH_DOMAIN "<secret>")
(goog-define DATABASE_URL "<secret>")
(goog-define PROJECT_ID "<secret>")
(goog-define STORAGE_BUCKET "<secret>")
(goog-define MESSAGING_SENDER_ID "<secret>")
(goog-define APP_ID "<secret>")
(goog-define MEASUREMENT_ID "<secret>")

(defonce firebase-app-info {:apiKey            API_KEY,
                            :authDomain        AUTH_DOMAIN,
                            :databaseURL       DATABASE_URL,
                            :projectId         PROJECT_ID,
                            :storageBucket     STORAGE_BUCKET,
                            :messagingSenderId MESSAGING_SENDER_ID
                            :appId             APP_ID
                            :measurementId     MEASUREMENT_ID})

(re-frame/reg-event-fx
  ::firebase-error
  (fn [_ error]
    (.log js/console
          (if (or (nil? error) (empty? error)) "firebase error!" (pprint error)))))

(defonce firebase-instance (atom nil))

(defn ^:export init []
  (when-not @firebase-instance
    (reset! firebase-instance
            (firebase/init :firebase-app-info firebase-app-info
                           :firestore-settings {}
                           :get-user-sub [::subs/user]
                           :set-user-event [::events/user]
                           :default-error-handler [::firebase-error]))))
