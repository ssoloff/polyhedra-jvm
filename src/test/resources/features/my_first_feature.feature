Feature: My First cucumber test
    In order to create BDD style selenium-webdriver tests
    As a tester
    I want to utilize cucumber

Scenario: Google search, using selenium
    Given I have navigated to google
    When I search for "selenium"
    Then the page title should be selenium - Google Search

