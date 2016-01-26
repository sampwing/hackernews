(ns hackernews.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [hackernews.core-test]))

(doo-tests 'hackernews.core-test)
