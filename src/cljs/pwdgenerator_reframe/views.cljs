(ns pwdgenerator-reframe.views
  (:require
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.domain :refer [password-stats]]
    [pwdgenerator-reframe.subs :as subs]
    [pwdgenerator-reframe.events :as events]
    [pwdgenerator-reframe.components :refer [save-personalized-params]]
    [pwdgenerator-reframe.styles :refer [card-classes container-classes]]
    [uikit]))

;; home

(defn on-field-change [params field event]
  (re-frame/dispatch [::events/params (assoc params field (-> event .-target .-value))]))

(defn form-field [field]
  (let [params (re-frame/subscribe [::subs/params])
        field-defs (re-frame/subscribe [::subs/field-defs])
        defs (field @field-defs)]
    ^{:key field}
    [:div {:id    (str field "-input")
           :class card-classes}
     [:label {:class [:uk-text-primary]} (:label defs)
      [:input {:class     :uk-input
               :type      :text
               :size      (:size defs)
               :maxLength (:maxlength defs)
               :value     (field @params)
               :on-change #(on-field-change @params field %)}]]]))

(defn form-fields []
  (let [field-defs (re-frame/subscribe [::subs/field-defs])]
    (map #(form-field %) (sort-by #(:order (% @field-defs)) (keys @field-defs)))))

(defn pwdgenerator []
  (let [value (re-frame/subscribe [::subs/value])
        params (re-frame/subscribe [::subs/params])
        show? (re-frame/subscribe [::subs/show?])]
    (fn []
      (let [stats (for [f password-stats] (f @value))]

        [:form
         [:div {:id    :form-container "uk-grid" 1
                :class container-classes}
          ^{:key :password}
          [:div {:id "password-container" :class (conj card-classes :uk-width-1-1)}
           [:label {:class [:uk-text-lead :uk-text-bolder :uk-text-primary]} "Password"]
           [:input {:id        "password-input"
                    :class     [:uk-input]
                    :type      (if @show? :text :password)
                    :value     @value
                    :on-change #(re-frame/dispatch [::events/value (-> % .-target .-value)])}]
           [:ul
            (doall
              (for [stat stats]
                ^{:key stat} [:li stat]))]]

          [:div {:id :button-group :class (conj card-classes :uk-width-1-1)}
           ^{:key "regenerate"}
           [:button {:id    :regenerate
                     :class [:uk-button "uk-width-3-5@m" :uk-button-small :uk-button-primary
                             :uk-box-shadow-medium :uk-box-shadow-hover-large]
                     :on-click
                            (fn [event]
                              (do
                                (.preventDefault event)
                                (re-frame/dispatch [::events/generate])))} "Regenerate"]
           ^{:key "reset"}
           [:button {:id    :reset
                     :class [:uk-button "uk-width-1-5@m" :uk-button-small :uk-button-secondary
                             :uk-box-shadow-medium :uk-box-shadow-hover-large]
                     :on-click
                            (fn [event]
                              (do
                                (.preventDefault event)
                                (re-frame/dispatch [::events/reset])))} "Reset"]
           ^{:key "copy"}
           [:button {:id    :copy
                     :class [:uk-button "uk-width-1-5@m" :uk-button-small :uk-button-danger
                             :uk-box-shadow-medium :uk-box-shadow-hover-large]
                     :on-click
                            (fn [event]
                              (do
                                (.preventDefault event)
                                (-> (.getElementById js/document "password-input") .select)
                                (.execCommand js/document "copy")))} "Copy"]]
          ^{:key "show-password-input"}
          [:div {:id    "show-password-input"
                 :class card-classes}
           [:label {:class [:uk-text-primary]}
            [:input {:class     :uk-checkbox
                     :type      :checkbox
                     :checked   @show?
                     :on-change #(re-frame/dispatch [::events/show? (-> % .-target .-checked)])}]
            " Show password?"]]
          (doall (form-fields))
          ^{:key "word-separator-input"}
          [:div {:id    "word-separator-input"
                 :class card-classes}
           [:label {:class [:uk-text-primary]}
            "Word Separator "
            [:input {:type      :text
                     :size      3
                     :maxLength 3
                     :value     (:word_separator @params)
                     :on-change #(on-field-change @params :word_separator %)}]
            " " (pr-str (:word_separator @params))]]
          [save-personalized-params]]]))))

(defn home-panel []
  (let [name @(re-frame/subscribe [::subs/name])]
    [:div {:class [:uk-background-muted :uk-flex :uk-flex-column :uk-flex-center
                   :uk-flex-top :uk-padding]}
     [:div {:class [:uk-margin-auto :uk-background-secondary :uk-light]}
      [:h1 {:class [:uk-padding]} name]]
     [pwdgenerator]

     [:div {:class [:uk-margin-auto :uk-text-small :uk-padding]}
      [:a {:class [:uk-link-muted] :href "#/about"}
       (str "About \"" name "\"")]]]))

;; about

(defn about-panel []
  (let [name @(re-frame/subscribe [::subs/name])]
    [:div {:class [:uk-background-muted :uk-flex :uk-flex-column :uk-flex-center
                   :uk-flex-top :uk-padding]}
     [:div {:class [:uk-margin-auto :uk-background-secondary :uk-light]}
      [:h1 {:class [:uk-padding]} (str "About \"" name "\"")]]

     [:div {:class container-classes}
      [:ul
       [:li "based on the idea that passwords with spaces are more secure, considering dictionary attacks"]
       [:li "generates a password that can be typed out a bit easier if necessary, considering virtual keyboards"]
       [:li "uses " [:a {:href "https://github.com/day8/re-frame"} "Reframe"]]
       [:li "uses " [:a {:href "https://getuikit.com/"} "UIkit"]]
       ]]

     [:div {:class [:uk-margin-auto :uk-text-small :uk-padding]}
      [:a {:class [:uk-link-muted] :href "#/"}
       (str "Goto " name)]]]))

;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :about-panel [about-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    (.grid uikit (.getElementById js/document "form-container"))
    [show-panel @active-panel]))
