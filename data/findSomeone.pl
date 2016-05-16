use threads;
use threads::shared;

my %results : shared;
my $target = "kurmasz";

%results = ();

sub find_on_machine {
    my $person = shift @_;
    my $machine = shift @_;

    my $output = `ssh $machine who | grep $person`;

    if ($output) {
        $results{$machine} = "yes";
    }
    return $machine;

}

@threads = ();
%clusters = (arch => 10, eos => 32 );

while (my ($name, $number) = each(%clusters)) {

    for (my $i = 1; $i <=$number; $i++) {
        next if $i == 24 || $i == 31;
        my $machine = sprintf("%s%02d.cis.gvsu.edu", $name, $i);
        push(@threads, threads->create(\&find_on_machine, $target, $machine));
    }
}

while (@threads > 0) {
    my $t = shift @threads;
    next unless $t;
    $t->join();
 }

my $list ="";
foreach (sort keys %results) {
    $list .= "<li>$_</li>\n";
}


my $document=<<"DONE";
<html>
  <head>
    <title>Where's Bentley'</title>
  </head>
  <body>
    <h1>Where's Bentley?</h1>
    Joseph is logged into:
    <ul>
        $list
    </ul>
  </body>
</html>
DONE


print $document;