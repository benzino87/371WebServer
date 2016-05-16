

my $document = <<"DONE";
<html>
  <head>
    <title>Hello, World</title>
  </head>
  <body>
    <h1>Hello, World!</h1>
    I'm writing my first pseudo-CGI web server
  </body>
</html>
DONE

print $document;