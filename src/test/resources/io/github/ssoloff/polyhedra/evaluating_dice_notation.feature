Feature: Evaluating dice notation
    In order to simplify tool development
    As a tool developer
    I want to be able to evaluate a dice notation expression

Background: The library produces deterministic results
    Given a random number generator that always generates the maximum value

Scenario: Evaluating malformed expressions
    Given the expression "<<INVALID>>"
    When the expression is evaluated
    Then an exception should be raised

Scenario: Evaluating constants
    Given the expression "5"
    When the expression is evaluated
    Then the expression result value should be 5

Scenario Outline: Evaluating expressions containing array literals
    Given the expression "<expression>"
    When the expression is evaluated
    Then the expression result value should be <result value>
    Examples:
        | expression          | result value |
        | sum([1])            | 1            |
        | sum([1, 2])         | 3            |
        | sum([1 + 1, 3 - 1]) | 4            |

Scenario Outline: Evaluating arithmetic expressions with constants
    Given the expression "<expression>"
    When the expression is evaluated
    Then the expression result value should be <result value>
    Examples:
        | expression | result value       |
        | 4 + 3      | 7                  |
        | 3 + 4      | 7                  |
        | 4 - 3      | 1                  |
        | 3 - 4      | -1                 |
        | 4 * 3      | 12                 |
        | 3 * 4      | 12                 |
        | 4 / 3      | 1.3333333333333333 |
        | 3 / 4      | 0.75               |
        | 4 % 3      | 1                  |
        | 3 % 4      | 3                  |

Scenario Outline: Evaluating dice rolls
    Given the expression "<expression>"
    When the expression is evaluated
    Then the expression result value should be <result value>
    Examples:
        | expression       | result value |
        | 3d6              | 18           |
        | 1d10             | 10           |
        | 1d%              | 100          |
        | sum(roll(2, d8)) | 16           |

Scenario Outline: Evaluating modified dice rolls
    Given the expression "<expression>"
    When the expression is evaluated
    Then the expression result value should be <result value>
    Examples:
        | expression                          | result value |
        | 2d6+L                               | 18           |
        | 2d6+2L                              | 24           |
        | 2d6+H                               | 18           |
        | 2d6+2H                              | 24           |
        | 3d6-L                               | 12           |
        | 3d6-2L                              | 6            |
        | 3d6-H                               | 12           |
        | 3d6-2H                              | 6            |
        | sum(cloneLowestRolls([1, 6], 1))    | 8            |
        | sum(cloneHighestRolls([1, 6], 1))   | 13           |
        | sum(dropLowestRolls([1, 3, 6], 1))  | 9            |
        | sum(dropHighestRolls([1, 3, 6], 1)) | 4            |

Scenario Outline: Evaluating arithmetic expressions with dice rolls and constants
    Given the expression "<expression>"
    When the expression is evaluated
    Then the expression result value should be <result value>
    Examples:
        | expression | result value |
        | 3d6 + 4    | 22           |
        | 3d6 - 4    | 14           |
        | 4 * 3d6    | 72           |
        | 3d6 / 4    | 4.5          |
        | 3d6 % 4    | 2            |
        | 1d% % 3    | 1            |
        | 2d6-L - 1  | 5            |
        | 1d6+L + 1  | 13           |

Scenario Outline: Rounding fractional values
    Given the expression "<expression>"
    When the expression is evaluated
    Then the expression result value should be <result value>
    Examples:
        | expression     | result value |
        | ceil(1 / 2)    | 1            |
        | floor(1 / 2)   | 0            |
        | round(1 / 3)   | 0            |
        | round(1 / 2)   | 1            |
        | trunc(1 / 2)   | 0            |
        | trunc(3d6 / 4) | 4            |

Scenario Outline: Evaluating grouped expressions
    Given the expression "<expression>"
    When the expression is evaluated
    Then the expression result value should be <result value>
    Examples:
        | expression    | result value |
        | 3 * (2 + 1)   | 9            |
        | (3d6 + 1) * 2 | 38           |

Scenario Outline: Evaluating division expressions with extended divide and round operators
    Given the expression "<expression>"
    When the expression is evaluated
    Then the expression result value should be <result value>
    Examples:
        | expression | result value |
        | 1 // 2     | 0            |
        | 3d6 // 4   | 4            |
        | 1 /~ 3     | 0            |
        | 1 /~ 2     | 1            |
        | 1 /- 2     | 0            |
        | 1 /+ 2     | 1            |

Scenario Outline: Evaluating expressions with unary operators
    Given the expression "<expression>"
    When the expression is evaluated
    Then the expression result value should be <result value>
    Examples:
        | expression | result value |
        | -1         | -1           |
        | +1         | 1            |
        | -3d6       | -18          |

Scenario Outline: Evaluating expressions that result in non-finite values
    Given the expression "<expression>"
    When the expression is evaluated
    Then an exception should be raised
    Examples:
        | expression  |
        | d6          |
        | [1, 2, 3]   |
        | round(d6)   |
        | roll(3, d6) |

