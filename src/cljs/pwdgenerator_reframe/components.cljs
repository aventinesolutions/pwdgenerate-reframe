(ns pwdgenerator-reframe.components
  (:require
    [cljs.pprint :refer [pprint]]
    [reagent.core :refer [atom]]
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.styles :refer [card-classes]]
    [pwdgenerator-reframe.subs :as subs]
    [pwdgenerator-reframe.events :as events]))

(defn save-personalized-params []
  (let [user (re-frame/subscribe [::subs/user])
        s (atom {:credentials {:email nil :password nil}})]
    ^{:key :save-personalized-params}
    [:div {:id    :save-personalized-params
           :class card-classes}
     [:h4.uk-text-primary "Personalized Params" [:span {:data-uk-icon "icon: check"}]]
     [:fieldset.uk-form-stacked.uk-padding
      [:label.text-primary "email address"
       [:input.uk-input {:type          :text
                         :auto-complete :email
                         :on-change
                                        (fn [event]
                                          (do
                                            (.preventDefault event)
                                            (swap! s (assoc-in @s [:credentials :email] (-> event .-target .-value)))))}]]
      [:label.text-primary "password"
       [:input.uk-input {:type          :password
                         :auto-complete :current-password
                         :on-change
                                        (fn [event]
                                          (do
                                            (.preventDefault event)
                                            (swap! s (assoc-in @s [:credentials :password] (-> event .-target .-value)))))}]]
      [:button.uk-button-primary {:on-click
                                  (fn [event]
                                    (do
                                      (.preventDefault event)
                                      (pprint s)
                                      (re-frame/dispatch [::events/firebase-login (:credentials @s)])))}
       "login"]]]))
