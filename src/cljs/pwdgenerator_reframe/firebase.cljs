(ns pwdgenerator-reframe.firebase
  (:require
    ["firebase" :as Firebase]))

(defonce firebase-instance (atom nil))

(defn init [config]
  (when-not @firebase-instance
    (reset! firebase-instance (-> Firebase (.initializeApp (clj->js config))))))

(defn email-sign-in [[email password]]
  (-> Firebase
      .auth
      (.signInWithEmailAndPassword email password)))

(defn current-user []
  (-> Firebase .auth .-currentUser))


