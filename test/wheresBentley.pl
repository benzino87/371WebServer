use threads;
use threads::shared;

my %results : shared;
my $target = "bentley";

%results = ();

# Look for $person on $machine.
# If the person is logged into that machine, add an entry for $machine in
# the global %results hash.
sub find_on_machine {
    my $person = shift @_;
    my $machine = shift @_;

    my $output = `ssh $machine who | grep $person`;

    # if $person isn't logged into $machine, then the
    # command won't have any output, thus $output will
    # be "falsy"

    if ($output) {
        $results{$machine} = "yes";
    }
    return $machine;
}

@threads = ();  # store the threads we create

%clusters = (arch => 10, eos => 32 );

while (my ($name, $number) = each(%clusters)) {
    for (my $i = 1; $i <=$number; $i++) {

        # eos 24 and 31 appear to be down right now
        next if $i == 24 || $i == 31;

        my $machine = sprintf("%s%02d.cis.gvsu.edu", $name, $i);

        # Launch a tread and save a handle to it in @threads
        push(@threads, threads->create(\&find_on_machine, $target, $machine));
    }
}

# wait for each thread to complete.
while (@threads > 0) {
    my $t = shift @threads;
    next unless $t;
    $t->join();
 }

# Build a string named $list that contains one
# entry for every machine the target person is logged into.
my $list ="";
foreach (sort keys %results) {
    $list .= "<li>$_</li>\n";
}

# interpolate $list into the desired web page.

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