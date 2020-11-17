Feature: Change orders details from Preparing status

  Background: 
    Given User navigates to home page
    And User selects order in Preparing status

  @Table18
  Scenario: Promote order from Preparing to Ready status
    When User promotes order to Ready status in kitchen
    Then Order status in table list should be Ready
    And User selects table
    And Promoted order details should be displayed
    And Order should be available in Ready status in kitchen
    And Order should be available in Ready status in search
    And Order should be available in Ready status in server

  @Table19
  Scenario: Cancel(Billed) order from Preparing status
    When User cancels the order
    Then Updated order details should be displayed
    And Table should be vacant in table list
    And Order should be available in Billed status in search
    And Order should be available in Billed status in server
    And Order should not be available in kitchen

  @Table20
  Scenario: Cancel order cancellation from Preparing status
    And User cancels the order cancellation
    Then Order details should not be updated
    And Order status in table list should be Preparing
    And Order should be available in Preparing status in search
    And Order should be available in Preparing status in kitchen
    And Order should not be available in server

  @Table21
  Scenario: Add order dish quantity in Preparing status
    When User adds following quantitiy to existing dishes
      | dish                | qty |
      | Tiramisu            |   2 |
      | Sausage Pappardelle |   3 |
    Then Alert is displayed with order updation message
    And Updated order details should be displayed
    And Order status in table list should be Preparing
    And Order should be available in Preparing status in search
    And Order should be available in Preparing status in kitchen
    And Order should not be available in server

  @Table22
  Scenario: Add new dish in Preparing status
    And User adds following new dishes
      | dish                   | qty |
      | Spaghetti Aglio E Olio |   3 |
      | Chicken Alla Diavola   |   1 |
      | Cannoli                |   2 |
    Then Alert is displayed with order updation message
    And Updated order details should be displayed
    And Order status in table list should be Preparing
    And Order should be available in Preparing status in search
    And Order should be available in Preparing status in kitchen
    And Order should not be available in server

  @Table23
  Scenario: Subtract order dish quantity in Preparing status
    When User subtracts following quantity from existing dishes
      | dish                | qty |
      | Chicken Milanese    |   1 |
      | Sausage Pappardelle |   2 |
    Then Alert is displayed with illegal dish quantity removal warning
    And Order details should not be updated
    And Order status in table list should be Preparing
    And Order should be available in Preparing status in search
    And Order should be available in Preparing status in kitchen
    And Order should not be available in server

  @Table24
  Scenario: Remove order dishes in Preparing status
    When User removes following existing dishes
      | Tiramisu            |
      | Sausage Pappardelle |
    Then Alert is displayed with illegal dish removal warning
    And Order details should not be updated
    And Order status in table list should be Preparing
    And Order should be available in Preparing status in search
    And Order should be available in Preparing status in kitchen
    And Order should not be available in server
