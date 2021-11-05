package edu.cnm.deepdive.vaccpocketkeeper.model.view;


import static edu.cnm.deepdive.vaccpocketkeeper.model.view.VaccineSummary.QUERY;

import androidx.room.DatabaseView;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;

@DatabaseView(value = QUERY, viewName = "dose_summary")
// a join or filtering of tables to show a combination of tables.
public class DoseSummary extends Dose {

  //"SELECT v.*, d.* FROM vaccine AS v INNER JOIN (SELECT * FROM dose GROUP BY vaccine_id) AS d ON v.vaccine_id = d.vaccine_id";
  static final String QUERY = "SELECT v.*, d.* \n"
      + "FROM vaccine AS v \n"
      + "INNER JOIN (SELECT * \n"
      + "FROM dose GROUP BY vaccine_id) AS d \n"
      + "ON v.vaccine_id = d.vaccine_id";
}

