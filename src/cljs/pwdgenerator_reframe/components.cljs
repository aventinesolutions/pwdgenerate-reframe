(ns pwdgenerator-reframe.components
  (:require
    [reagent.core :refer [atom]]
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.styles :refer [card-classes]]
    [pwdgenerator-reframe.subs :as subs]
    [pwdgenerator-reframe.events :as events]))

(defonce component-state (atom {}))

(defn save-personalized-params []
  (let []
    (fn []
      (let [user (re-frame/subscribe [::subs/user])
            email (:email @component-state)
            password (:password @component-state)]
        [:div
         {:id    :save-personalized-params
          :class card-classes}
         (pr-str user)
         [:h4.uk-text-primary "Personalized Params" [:span {:data-uk-icon "icon: check"}]]
         [:fieldset.uk-form-stacked.uk-padding
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
          [:button.uk-button-primary {:on-click
                                      (fn [event]
                                        (do
                                          (.preventDefault event)
                                          (re-frame/dispatch
                                            [::events/firebase-login [email password]])))}
           "login"]]]))))
