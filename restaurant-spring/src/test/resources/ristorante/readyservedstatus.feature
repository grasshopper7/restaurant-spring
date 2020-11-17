Feature: Change status details from Ready and Served statuses

	@Table27
  Scenario: Promote order from Ready to Served status
    Given User navigates to home page
    And User selects order in Ready status
    When User promotes order to Served status in server
    Then Order should be available in Served status in server
    And Order should not be available in kitchen
    And Order status in table list should be Served
    And User selects table
    And Promoted order details should be displayed

	@Table28
  Scenario: Promote order from Served to Billed status
    Given User navigates to home page
    And User selects order in Served status
    When User promotes order to Billed status in server
    And Order should be available in Billed status in server
    Then Order should not be available in kitchen
    And Table should be vacant in table list
    And Order should be available in Billed status in search
    And User selects Billed order from search
    And Promoted order details should be displayed