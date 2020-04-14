(ns pwdgenerator-reframe.components
  (:require
    [pwdgenerator-reframe.styles :refer [card-classes]]
    [pwdgenerator-reframe.subs :as subs]
    [pwdgenerator-reframe.events :as events]
    ))

(defn save-personalized-params []
  ^{:key :save-personalized-params}
  [:div {:id :save-personalized-params
         :class card-classes}
   [:h4.uk-text-primary "Personalized Params" [:span {:uk-icon "icon: check"}]]
   [:fieldset.uk-form-stacked.uk-padding
    [:label.text-primary "email address" [:input.uk-input {:type :text}]]
    [:label.text-primary "password" [:input.uk-input {:type :password}]]
    [:button.uk-button-primary "login"]]])
