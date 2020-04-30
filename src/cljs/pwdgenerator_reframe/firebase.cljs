(ns pwdgenerator-reframe.firebase
  (:require
    [cljs.pprint :refer [pprint]]
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.subs :as subs]
    [pwdgenerator-reframe.events :as events]
    [pwdgenerator-reframe.db :as db]
    [com.degel.re-frame-firebase :as firebase]))

(defonce firebase-app-info {})

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
