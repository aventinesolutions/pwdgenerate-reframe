(ns pwdgenerator-reframe.components.save-personalized-params
  (:require
    [reagent.core :refer [atom]]
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.styles :refer [card-classes]]
    [pwdgenerator-reframe.subs :as subs]
    [pwdgenerator-reframe.firebase :as firebase]))

(defonce component-state (atom {}))

(defn save-personalized-params []
  (fn []
    (let [user (re-frame/subscribe [::subs/user])
          email (:email @component-state)
          password (:password @component-state)]
      [:div
       {:id    :save-personalized-params
        :class card-classes}
       [:h4.uk-text-primary
        [:span.uk-icon {:data-uk-icon "icon: heart"}]
        " Personalized Params "
        [:span.uk-text-small (:email @user)]]
       [:fieldset.uk-form-stacked.uk-padding
        {:on-focus
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
                           (re-frame/dispatch [::firebase/sign-in-by-email [email password]]))}
         "login"]]])))
