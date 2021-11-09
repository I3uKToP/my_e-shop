Feature: Category

  Scenario Outline: Successful Login to the page and logout after
    Given I open web browser
    When I navigate to login.html page
    And I provide username as "<username>" and password as "<password>"
    And I click on login button
    When I navigate to category
    And I add new category
    And I input new category as <category>
    Then category name should be <category>


    Examples:
      | username | password | category |
      | admin | 123 | test |
