Feature: Find the username for each user first name provided to the application

  Scenario: With a Postgres database
    Given a Postgres database with users
    When a user runs the console application
    Then the application will log the following messages:
      | level | message                                            |
      | INFO  | Usernames found: [testUser1, testUser2, testUser3] |

  Scenario: With an MSSQL database
    Given an MSSQL database with users
    When a user runs the console application
    Then the application will log the following messages:
      | level | message                                            |
      | INFO  | Usernames found: [testUser4, testUser5, testUser6] |