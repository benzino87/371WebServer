

my $line ="";
foreach (sort keys %ENV) {
    $line .= "<tr><td>$_</td><td>$ENV{$_}</td></tr>\n";
}

my $document = <<"DONE";
<html>
    <head>
        <title>List of Environment Variables</title>
        <style type="text/css">
            th, td {
                padding-right: 15px;
                text-align: left;
            }

            th {
             border-bottom: 1px solid;
             }
          </style>
    </head>
    <body>
    <h1>Environment Variables</h1>
    <table>
        <tr><th>Key</th><th>Value</th></tr>
        $line
    </table>
    </body>
</html>

DONE

print $document;