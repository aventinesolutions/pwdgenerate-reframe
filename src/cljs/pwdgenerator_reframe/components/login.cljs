(ns pwdgenerator-reframe.components.login
  (:require
    [reagent.core :refer [atom]]
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.events :as events]
    [pwdgenerator-reframe.styles :refer [card-classes]]
    [pwdgenerator-reframe.firebase :as firebase]))

(defonce component-state (atom {}))

(defn login []
  (fn []
    (let [email (:email @component-state)
          password (:password @component-state)]
      ^{:key :login}
      [:fieldset#login
       {:class [:uk-form-stacked :uk-padding-small]
        :on-focus
        (fn [event]
          (do
            (.preventDefault event)
            (firebase/init)))}
       [:label.text-primary "email address"
        [:input.uk-input {:type          :text
                          :auto-complete :email

                          :on-change     (fn [event]
                                           (do
                                             (.preventDefault event)
                                             (swap! component-state assoc :email (-> event .-target .-value))))}]]
       [:label.text-primary "password"
        [:input.uk-input {:type          :password
                          :auto-complete :current-password
                          :on-change     (fn [event]
                                           (do
                                             (.preventDefault event)
                                             (swap! component-state assoc :password (-> event .-target .-value))))}]]
       [:button {:class [:uk-button :uk-button-primary :uk-button-small]
                 :on-click
                        (fn [event]
                          (.preventDefault event)
                          (re-frame/dispatch [::events/sign-in-by-email [email password]]))} "login"]])))
