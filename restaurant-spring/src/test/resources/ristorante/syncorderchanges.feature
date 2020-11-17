Feature: Order change messages to be synced across browser windows

  #PROMOTE
  @Table31 
  Scenario: 1 browser - Promote order status with same order selected
    Given User navigates to home page
    And User selects order in Ordered status
    When User promotes order to Preparing status in kitchen
    Then Order status in table list should be Preparing
    And Order status text in table list should be highlighted
    And Modification message should be displayed in order details
    And Order modify and cancel buttons should be disabled

  #PROMOTE
  @Table32
  Scenario: 1 browser - Promote order status with another order selected
    Given User navigates to home page
    And User selects control order
    When User promotes order to Preparing status in kitchen
    Then Order status in table list should be Preparing
    And Order status text in table list should not be highlighted
    And Modification message should not be displayed in order details
    And Order modify and cancel buttons should not be disabled

  #PROMOTE
  @Table33
  Scenario: 2 browser - Promote order status with same order selected in control browser
    Given User navigates to home page in similar control browser
    And User selects order in Ordered status in similar control browser
    And User navigates to home page
    And User selects order in Ordered status
    When User promotes order to Preparing status in kitchen
    Then Order status in table list should be Preparing
    And Order status text in table list should be highlighted
    And Modification message should be displayed in order details
    And Order modify and cancel buttons should be disabled
    And Order status in table list should be Preparing in similar control browser
    And Order status text in table list should be highlighted in similar control browser
    And Modification message should be displayed in order details in similar control browser
    And Order modify and cancel buttons should be disabled in similar control browser

  #UPDATE
  @Table34
  Scenario: 2 browser - Update order details with same order selected in control browser
    Given User navigates to home page in similar control browser
    And User selects order in Ordered status in similar control browser
    And User navigates to home page
    And User selects order in Ordered status
    When User adds following quantitiy to existing dishes
      | dish                | qty |
      | Tiramisu            |   2 |
      | Sausage Pappardelle |   3 |
    And Order status text in table list should be highlighted in similar control browser
    And Modification message should be displayed in order details in similar control browser
    And Order modify and cancel buttons should be disabled in similar control browser

  #UPDATE
  @Table35
  Scenario: 2 browser - Update order details with another table selected in control browser
    Given User navigates to home page in similar control browser
    And User selects control order in similar control browser
    And User navigates to home page
    And User selects order in Ordered status
    When User adds following quantitiy to existing dishes
      | dish                | qty |
      | Tiramisu            |   2 |
      | Sausage Pappardelle |   3 |
    And Order status text in table list should not be highlighted in similar control browser
    And Modification message should not be displayed in order details in similar control browser
    And Order modify and cancel buttons should not be disabled in similar control browser

  #PROMOTE
  @Table36
  Scenario: 2+1 browser - Promote order status with same order selected in both control browsers
    Given User navigates to home page in similar control browser
    And User selects order in Ordered status in similar control browser
    And User navigates to home page in different control browser
    And User selects order in Ordered status in different control browser
    And User navigates to home page
    And User selects order in Ordered status
    When User promotes order to Preparing status in kitchen
    Then Order status in table list should be Preparing
    And Order status text in table list should be highlighted
    And Modification message should be displayed in order details
    And Order modify and cancel buttons should be disabled
    And Order status in table list should be Preparing in similar control browser
    And Order status text in table list should be highlighted in similar control browser
    And Modification message should be displayed in order details in similar control browser
    And Order modify and cancel buttons should be disabled in similar control browser
    And Order status in table list should be Preparing in different control browser
    And Order status text in table list should be highlighted in different control browser
    And Modification message should be displayed in order details in different control browser
    And Order modify and cancel buttons should be disabled in different control browser

  #UPDATE
  @Table37
  Scenario: 2+1 browser - Update order details with same order selected in both control browsers
    Given User navigates to home page in similar control browser
    And User selects order in Ordered status in similar control browser
    And User navigates to home page in different control browser
    And User selects order in Ordered status in different control browser
    And User navigates to home page
    And User selects order in Ordered status
    When User adds following quantitiy to existing dishes
      | dish                | qty |
      | Tiramisu            |   2 |
      | Sausage Pappardelle |   3 |
    And Order status text in table list should be highlighted in similar control browser
    And Modification message should be displayed in order details in similar control browser
    And Order modify and cancel buttons should be disabled in similar control browser
    And Order status text in table list should be highlighted in different control browser
    And Modification message should be displayed in order details in different control browser
    And Order modify and cancel buttons should be disabled in different control browser

  #UPDATE
  @Table38
  Scenario: 2+1 browser - Update order details with same order selected in similar control browsers and another order in different browser
    Given User navigates to home page in similar control browser
    And User selects order in Ordered status in similar control browser
    And User navigates to home page in different control browser
    And User selects control order in different control browser
    Given User navigates to home page
    And User selects order in Ordered status
    When User adds following quantitiy to existing dishes
      | dish                | qty |
      | Tiramisu            |   2 |
      | Sausage Pappardelle |   3 |
    And Order status text in table list should be highlighted in similar control browser
    And Modification message should be displayed in order details in similar control browser
    And Order modify and cancel buttons should be disabled in similar control browser
    And Order status text in table list should not be highlighted in different control browser
    And Modification message should not be displayed in order details in different control browser
    And Order modify and cancel buttons should not be disabled in different control browser

  #UPDATE
  @Table39
  Scenario: 2+1 browser - Update order details with another order selected in both control browsers
    Given User navigates to home page in similar control browser
    And User selects control order in similar control browser
    And User navigates to home page in different control browser
    And User selects control order in different control browser
    Given User navigates to home page
    And User selects order in Ordered status
    When User adds following quantitiy to existing dishes
      | dish                | qty |
      | Tiramisu            |   2 |
      | Sausage Pappardelle |   3 |
    And Order status text in table list should not be highlighted in similar control browser
    And Modification message should not be displayed in order details in similar control browser
    And Order modify and cancel buttons should not be disabled in similar control browser
    And Order status text in table list should not be highlighted in different control browser
    And Modification message should not be displayed in order details in different control browser
    And Order modify and cancel buttons should not be disabled in different control browser
