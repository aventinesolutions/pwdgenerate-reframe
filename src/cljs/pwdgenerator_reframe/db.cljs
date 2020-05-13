(ns pwdgenerator-reframe.db
  (:require
    [pwdgenerator-reframe.domain :refer [generate-pw]]))

(def defaults
  {:no_words           5
   :uppercase          "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
   :no_uppercase_alpha 5
   :lowercase          "abcdefghijklmnopqrstuvwxyz"
   :no_lowercase_alpha 5
   :symbols            "!@#$&*.:?+="
   :no_symbols         1
   :numerics           "0123456789"
   :no_numerics        3
   :word_separator     " "})

(def form-field-defs
  {:no_words           {:order     1
                        :label     "Number of Words "
                        :size      3
                        :maxlength 3
                        :numeric?  true}
   :uppercase          {:order     2
                        :label     "Upper Case Alpha Character Set "
                        :size      35
                        :maxlength 26
                        :numeric?  false}
   :no_uppercase_alpha {:order     3
                        :label     "Number of Upper Case Alpha Characters "
                        :size      3
                        :maxlength 3
                        :numeric?  true}
   :lowercase          {:order     4
                        :label     "Lower Case Alpha Character Set "
                        :size      35
                        :maxlength 26
                        :numeric?  false}
   :no_lowercase_alpha {:order     5
                        :label     "Number of Lower Case Alpha Characters "
                        :size      3
                        :maxlength 3
                        :numeric?  true}
   :numerics           {:order     6
                        :label     "Numeric Character Set "
                        :size      10
                        :maxlength 10
                        :numeric?  false}
   :no_numerics        {:order     7
                        :label     "Number of Numeric Characters "
                        :size      3
                        :maxlength 3
                        :numeric?  true}
   :symbols            {:order     8
                        :label     "Symbol Character Set "
                        :size      10
                        :maxlength 10
                        :numeric?  false}
   :no_symbols         {:order     9
                        :label     "Number of Symbol Characters "
                        :size      3
                        :maxlength 3
                        :numeric?  true}})

(def default-db
  {:name       "Password Generator in Re-frame"
   :value      (generate-pw defaults)
   :show?      true
   :params     defaults
   :field-defs form-field-defs
   :user       nil})
