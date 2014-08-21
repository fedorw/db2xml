db2xml
======

populate xml from jdbc databases with templates like this:


<?xml version="1.0"?>
<ff>
    <customer>
		select Username, Email,CustomerID from customer
			<licenses push="CustomerID" mute="true">
	       	     select ? as id
				<license push="id">
				select SoftwareName from customer_license where CustomerID=?
				</license>
			</licenses>
		</customer>
</ff>
