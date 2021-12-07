<!DOCTYPE html>
<#import "spring.ftl" as spring/>
<head>
    <meta charset="UTF-8">
    <title>SignUp</title>
    <style>
        .error {
            color: red;
        }
    </style>
</head>
<body>
<h1>Sign Up Page</h1>
<h2>Please Enter your data:</h2>
<form action="/signUp" method="post">
    <@spring.bind path="signUpForm"/>
    <p class="error">${spring.status.errorMessage}</p>
    <label for="firstName">Enter first name </label>
    <@spring.formInput 'signUpForm.firstName' 'id="firstName" class="text" placeholder="Your first name"'/>
    <@spring.showErrors '<br>' 'error' />
    <br>
    <br>
    <label for="lastName">Enter last name:</label>
    <@spring.formInput 'signUpForm.lastName' 'id="lastName" class="text" placeholder="Your last name"'/>
    <@spring.showErrors '<br>' 'error' />
    <br>
    <br>
    <label for="country">Enter country:</label>
    <@spring.formInput 'signUpForm.country' 'id="country" class="text" placeholder="Your country"'/>
    <@spring.showErrors '<br>' 'error' />
    <br>
    <br>
    <label for="city">Enter city:</label>
    <@spring.formInput 'signUpForm.city' 'id="city" class="text" placeholder="Your city"'/>
    <@spring.showErrors '<br>' 'error' />
    <br>
    <br>
    <label for="email">Enter email:</label>
    <@spring.formInput 'signUpForm.email' 'id="email" class="text" placeholder="Your email"'/>
    <@spring.showErrors '<br>' 'error' />
    <br>
    <br>
    <label for="password">Enter password:</label>
    <@spring.formInput 'signUpForm.password' 'id="password" class="text" placeholder="Your password"'/>
    <@spring.showErrors '<br>' 'error' />
    <br>
    <br>
    <input type="submit" value="Sign Up">
</form>
</body>
</html>
