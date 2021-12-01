package edu.cnm.deepdive.vaccpocketkeeper.model.view;


import static edu.cnm.deepdive.vaccpocketkeeper.model.view.VaccineSummary.QUERY;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;
import androidx.room.RewriteQueriesToDropUnusedColumns;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;

/**
 * Encapsulates a persistent VaccineSummary object DatabaseView that is a join of {@link Vaccine} and {@link Dose} by vaccine_id.
 */
@DatabaseView(value = QUERY, viewName = "vaccine_summary")
// a join or filtering of tables to show a combination of tables.
public class VaccineSummary extends Dose {

  //TODO: test this out
  //"SELECT v.*, d.* FROM vaccine AS v INNER JOIN (SELECT * FROM dose GROUP BY vaccine_id) AS d ON v.vaccine_id = d.vaccine_id";
  static final String QUERY = "SELECT v.*, d.* \n"
      + "FROM vaccine AS v \n"
      + "INNER JOIN (SELECT * \n"
      + "FROM dose GROUP BY vaccine_id) AS d \n"
      + "ON v.vaccine_id = d.vaccine_id";
}

