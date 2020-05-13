(ns pwdgenerator-reframe.components
  (:require
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.styles :refer [card-classes]]
    [pwdgenerator-reframe.subs :as subs]
    [pwdgenerator-reframe.events :as events]))

(defn save-personalized-params []
  (let [user (re-frame/subscribe [::subs/user])]
    ^{:key :save-personalized-params}
    [:div {:id    :save-personalized-params
           :class card-classes}
     [:h4.uk-text-primary "Personalized Params" [:span {:data-uk-icon "icon: check"}]]
     [:fieldset.uk-form-stacked.uk-padding
      [:label.text-primary "email address" [:input.uk-input {:type :text}]]
      [:label.text-primary "password" [:input.uk-input {:type :password}]]
      [:button.uk-button-primary {:on-click
                                  (fn [event]
                                    (do
                                      (.preventDefault event)
                                      (re-frame/dispatch [::events/firebase-login nil nil])))}
       "login"]]]))
