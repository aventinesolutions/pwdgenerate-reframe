(ns pwdgenerator-reframe.config)

(def debug?
  ^boolean goog.DEBUG)

(defmacro log
  [& msgs]
  `(.log js/console ~@msgs))

