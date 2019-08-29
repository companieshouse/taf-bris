@regression @add_business_register_validation
Feature: This feature is to verify the ability of the BR to detect and process invalid Add Business Register Notifications
  Acceptance Criteria

  As a BRIS User
  I would like A BR-BusinessError returned if a Add Business Register Notification fails validation
  So that the BR's system complies with BRIS 2.0

  Information:
  * Leave blank to keep default
  * Set to "NULL" to set as null
  * Set EMPTY for empty string


  Scenario Outline: Create AddBrNotification with various validation
    Given I am creating an AddBrNotification with details
      | companyNumber    | messageId    | correlationId    | receiverBusinessRegisterId      | receiverCountryCode     | registerName    | notificationDateTime     | senderBusinessRegisterId      | senderCountryCode     |
      | <company_number> | <message_id> | <correlation_id> | <receiver_business_register_id> | <receiver_country_code> | <register_name> | <notification_date_time> | <sender_business_register_id> | <sender_country_code> |
    Then I should get a add Br Notification error message with the error code <error_code>

    Examples:
      | company_number | message_id | correlation_id | receiver_business_register_id | receiver_country_code | register_name | notification_date_time | sender_business_register_id | sender_country_code | error_code  |
      |                |            | 123456789      |                               |                       |               |                        |                             |                     | ERR_BR_5103 |
      |                |            |                |                               | AT                    |               |                        |                             |                     | ERR_BR_0103 |
      |                |            |                | AB                            |                       |               |                        |                             |                     | ERR_BR_0103 |
      |                |            |                |                               | AB                    |               |                        |                             |                     | ERR_BR_5102 |
      |                |            |                |                               | EMPTY                 |               |                        |                             |                     | ERR_BR_5102 |
      |                |            | EMPTY          |                               |                       |               |                        |                             |                     | ERR_BR_5108 |
      |                |            | EMPTY          |                               |                       |               |                        |                             |                     | ERR_BR_5108 |
      |                |            |                |                               |                       |               | NULL                   |                             |                     | ERR_BR_5102 |
      |                |            |                |                               | EMPTY                 |               |                        |                             |                     | ERR_BR_5102 |
      |                |            |                |                               |                       | EMPTY         |                        |                             |                     | ERR_BR_5102 |
      |                |            |                |                               | EMPTY                 |               |                        |                             |                     | ERR_BR_5102 |
      |                |            |                |                               |                       |               | NULL                   |                             |                     | ERR_BR_5102 |
