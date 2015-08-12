Feature: Evaluating dice notation
    In order to simplify tool development
    As a tool developer
    I want to be able to evaluate a dice notation expression

Scenario: Evaluating malformed expressions
    Given the expression "<<INVALID>>"
    When the expression is evaluated
    Then an exception should be thrown

Scenario: Evaluating constants
    Given the expression "5"
    When the expression is evaluated
    Then the expression result value should be 5

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

Scenario Outline: Evaluating expressions with unary operators
    Given the expression "<expression>"
    When the expression is evaluated
    Then the expression result value should be <result value>
    Examples:
        | expression | result value |
        | +1         | 1            |

