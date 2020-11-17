Feature: Change status details from Vacant status

  Background: 
    Given User navigates to home page
    When User selects vacant table

  @Table5
  Scenario: Create new order
    And User creates new order by selecting dishes
      | dish                  | qty |
      | Fried Gnocchi         |   3 |
      | Margherita            |   1 |
      | Tagliatelle Bolognese |   4 |
      | Chicken Alla Diavola  |   2 |
    Then Alert is displayed with order creation message
    And Created order details should be displayed
    And Order status in table list should be Ordered
    And Order should be available in Ordered status in search
    And Order should be available in Ordered status in kitchen
    And Order should not be available in server

  @Table6
  Scenario: Create new order without any dish
    And User creates new order without selecting dish
    Then Alert is displayed with order creation warning
    And Order should not be created
    And Table should be vacant in table list
    And Order should not be available in kitchen
    And Order should not be available in server
