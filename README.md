# Small license server

An application that is supposed to limit access to a target web application to several instances.
A token in a storage -> instance of application that can be used simultaniously.

It is a responsibility of the target application to check periodically that token is still valid (aka. noone else accessed app with the same token).

This application is for training purposes, and it is for demonstration purposes only. 
It is known to have some breaches, some of them are intentional for simplifying demonstration.

## Deploying to Heroku

```sh
$ heroku create
$ git push heroku main
$ heroku open
``` 
