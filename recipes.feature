Feature: Recipes
  Background:
    * url 'https://api.spoonacular.com/recipes/'
    * def apiKey = '1ace6af9bfcd411cae06062c867a0c49'
    * def username = 'estiarista'
    * def hash = 'c59572f29c4524a803c6725f5f1f30953a9abefd'
    * def id = 640352

  Scenario: Search Recipe by Ingredients
    Given path 'findByIngredients'
    And param apiKey = apiKey
    And param ingredients = 'apples,+flour,+sugar'
    And param number = 2
    When method get
    Then status 200
    And print 'Response: ', response

  Scenario: Get Recipe Information
    Given path id, 'information'
    And param apiKey = apiKey
    And param includeNutrition = 'false'
    When method get
    Then status 200
    And print 'Response: ', response

  Scenario: Get Recipe Information if Providing an invalid recipe ID
    Given path '0', 'information'
    And param apiKey = apiKey
    And param includeNutrition = 'false'
    When method get
    Then status 404
    And print 'Response: ', response

  Scenario: Get Taste by ID
    Given path id, 'tasteWidget.json'
    And param apiKey = apiKey
    When method get
    Then status 200
    And print 'Response: ', response

  Scenario: Get Taste by ID if Providing an invalid recipe ID
    Given path '0', 'tasteWidget.json'
    And param apiKey = apiKey
    When method get
    Then status 404
    And print 'Response: ', response

  Scenario: Get Price Breakdown by ID
    Given path id, 'priceBreakdownWidget.json'
    And param apiKey = apiKey
    When method get
    Then status 200
    And print 'Response: ', response

  Scenario: Get Analyzed Recipe Instructions
    Given path id, 'analyzedInstructions'
    And param apiKey = apiKey
    When method get
    Then status 200
    And print 'Response: ', response

  Scenario: Analyze Recipe
    Given path 'analyze'
    And header Content-Type = 'appliaction/json'
    And param apiKey = apiKey
    And request
    """
    {
    "title": "Meatball",
    "servings": 2,
    "ingredients": [
        "1 pound (450g) ground beef",
        "1/2 cup breadcrumbs",
        "1/4 cup grated Parmesan cheese",
        "1/4 cup chopped fresh parsley",
        "1 egg",
        "2 cloves garlic, minced",
        "1 teaspoon salt",
        "1/2 teaspoon black pepper",
        "1/2 teaspoon dried oregano",
        "1/2 teaspoon dried basil",
        "1/4 teaspoon red pepper flakes (optional)",
        "Olive oil, for cooking"
    ],
    "instructions": "In a large mixing bowl, combine the ground beef, breadcrumbs, grated Parmesan cheese, chopped parsley, egg, minced garlic, salt, black pepper, dried oregano, dried basil, and red pepper flakes (if using). Mix everything together until well combined. Take a small amount of the meat mixture and roll it between your palms to form a meatball, about 1 to 1.5 inches in diameter. Repeat with the remaining mixture until you have formed all the meatballs.Heat a tablespoon of olive oil in a large skillet or frying pan over medium heat. Once the oil is hot, add the meatballs to the pan in a single layer, making sure not to overcrowd the pan. You may need to cook the meatballs in batches.Cook the meatballs for about 8 to 10 minutes, turning them occasionally with tongs, until they are browned on all sides and cooked through.Once the meatballs are cooked, remove them from the pan and place them on a plate lined with paper towels to drain any excess oil.Serve the meatballs hot with your favorite sauce, such as marinara sauce or Swedish meatball sauce, and enjoy! "
    }
    """
    When method post
    Then status 200
    And print 'Response: ', response
