Feature: Change orders details from Ordered status

  Background: 
    Given User navigates to home page
    And User selects order in Ordered status

  @Table8
  Scenario: Promote order from Ordered to Preparing status
    When User promotes order to Preparing status in kitchen
    Then Order status in table list should be Preparing
    And User selects table
    And Promoted order details should be displayed
    And Order should be available in Preparing status in kitchen
    And Order should be available in Preparing status in search
    And Order should not be available in server

  @Table9
  Scenario: Cancel order from Ordered status
    When User cancels the order
    Then Updated order details should be displayed
    And Table should be vacant in table list
    And Order should be available in Cancelled status in search
    And Order should be available in Cancelled status in kitchen
    And Order should not be available in server

  @Table10
  Scenario: Cancel order cancellation from Ordered status
    And User cancels the order cancellation
    Then Order details should not be updated
    And Order status in table list should be Ordered
    And Order should be available in Ordered status in search
    And Order should be available in Ordered status in kitchen
    And Order should not be available in server

  @Table11
  Scenario: Add order dish quantity in Ordered status
    When User adds following quantitiy to existing dishes
      | dish                | qty |
      | Tiramisu            |   2 |
      | Sausage Pappardelle |   3 |
    Then Alert is displayed with order updation message
    And Updated order details should be displayed
    And Order status in table list should be Ordered
    And Order should be available in Ordered status in search
    And Order should be available in Ordered status in kitchen
    And Order should not be available in server

  @Table12
  Scenario: Add new dish in Ordered status
    And User adds following new dishes
      | dish                   | qty |
      | Spaghetti Aglio E Olio |   3 |
      | Chicken Alla Diavola   |   1 |
      | Cannoli                |   2 |
    Then Alert is displayed with order updation message
    And Updated order details should be displayed
    And Order status in table list should be Ordered
    And Order should be available in Ordered status in search
    And Order should be available in Ordered status in kitchen
    And Order should not be available in server

  @Table13
  Scenario: Subtract order dish quantity in Ordered status
    When User subtracts following quantity from existing dishes
      | dish                | qty |
      | Chicken Milanese    |   1 |
      | Sausage Pappardelle |   2 |
    Then Alert is displayed with order updation message
    And Updated order details should be displayed
    And Order status in table list should be Ordered
    And Order should be available in Ordered status in search
    And Order should be available in Ordered status in kitchen
    And Order should not be available in server

  @Table14
  Scenario: Remove order dishes in Ordered status
    When User removes following existing dishes
      | Tiramisu            |
      | Sausage Pappardelle |
    Then Alert is displayed with order updation message
    And Updated order details should be displayed
    And Order status in table list should be Ordered
    And Order should be available in Ordered status in search
    And Order should be available in Ordered status in kitchen
    And Order should not be available in server

  @Table15
  Scenario: Remove order dishes by subtracting dishes in Ordered status
    When User subtracts quantity till zero from following existing dishes
      | Chicken Milanese    |
      | Sausage Pappardelle |
    Then Alert is displayed with order updation message
    And Updated order details should be displayed
    And Order status in table list should be Ordered
    And Order should be available in Ordered status in search
    And Order should be available in Ordered status in kitchen
    And Order should not be available in server
