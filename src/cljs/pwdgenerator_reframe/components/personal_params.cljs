(ns pwdgenerator-reframe.components.personal-params
  (:require
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.styles :refer [card-classes]]
    [pwdgenerator-reframe.events :as events]))

(def document-path [:params])

(defn personal-params []
  (fn []
    (let [params (re-frame/subscribe [:firebase/on-value {:path document-path}])]
      ^{:key :personal-params}
      [:div#personal-params
       [:h4 (pr-str @params)]
       [:fieldset#logoff
        {:class [:uk-form-stacked :uk-padding-small]}
        [:button {:class [:uk-button :uk-button-primary :uk-button-small]
                  :on-click
                         (fn [event]
                           (.preventDefault event)
                           (re-frame/dispatch [::events/sign-out]))} "logoff"]]])))
