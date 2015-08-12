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
    Then the expression result value should be "5"

