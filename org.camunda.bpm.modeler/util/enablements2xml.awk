# Simple awk script to convert Tool Enablement settings to something more suitable for inclusion in plugin.xml
BEGIN {
	FS = "[\\.=]";
	OFS = "";
	skip = "";
}

{
	if (NF==2)
	{
		# start of a new object
		if ($2 ~ "false")
		{
			skip = $1;
		}
		else
		{
			print "\t\t\t<enable object=\"", $1, "\"/>"
			skip = "";
		}
	}
	else if (NF==3)
	{
		if (skip=="" && $3 ~ "false")
		{
			# some feature of current object
			print "\t\t\t<disable object=\"", $1, "\" feature=\"", $2, "\"/>"
		}			
	}
}
