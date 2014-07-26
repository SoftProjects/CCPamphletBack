package com.comicon.pamphlet.data.dataBase;

import com.common.database.DbTable;

public class CircleTable extends DbTable {

	protected CircleTable() {
		super("circletable");
		addColumns("cid", collumsType.INTEGER);
		addColumns("name", collumsType.TEXT);
		addColumns("mode", collumsType.TEXT);
		addColumns("site", collumsType.TEXT);
		addColumns("property", collumsType.TEXT);
		addColumns("boothnum", collumsType.TEXT);
		addColumns("c_order", collumsType.TEXT);
	}
}
