(ns pwdgenerator-reframe.components.personal-params
  (:require
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.styles :refer [card-classes]]
    [pwdgenerator-reframe.firebase :as firebase]))

(defn personal-params []
  ^{:key :personal-params}
  [:fieldset#personal-params
   {:class [:uk-form-stacked :uk-padding-small]}
   [:button {:class [:uk-button :uk-button-primary :uk-button-small]
             :on-click
                    (fn [event]
                      (.preventDefault event)
                      (re-frame/dispatch [::firebase/sign-out]))}
    "logoff"]])
