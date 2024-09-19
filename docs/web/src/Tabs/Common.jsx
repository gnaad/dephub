import { makeStyles } from "@material-ui/core";
import Backdrop from "@material-ui/core/Backdrop";
import CircularProgress from "@material-ui/core/CircularProgress";
import Grid from "@material-ui/core/Grid";
import React, { useEffect, useState } from "react";
import DependencyCard from "../Cards/DependencyCard";

const useStyle = makeStyles((theme) => ({
  backdrop: {
    zIndex: theme.zIndex.drawer + 1,
    color: "#fff",
  },
}));

export default function Common({ dependencyType }) {
  const [dependency, setDependency] = useState([]);
  const classes = useStyle();

  useEffect(() => {
    fetch("https://gnaad.github.io/dephub/json/dependency.json")
      .then((result) => result.json())
      .then((response) => setDependency(response));
  }, []);

  return (
    <div>
      {dependency.length === 0 ? (
        <Backdrop className={classes.backdrop} open={true}>
          <CircularProgress color="primary" />
        </Backdrop>
      ) : (
        <Grid container spacing={2}>
          {dependency
            .sort((a, b) => (a.dependency_name > b.dependency_name ? 1 : -1))
            .filter((data) => data.type === dependencyType)
            .map((data) => (
              <Grid item xs={12} md={6} lg={4} key={data.id}>
                <DependencyCard data={data} />
              </Grid>
            ))}
        </Grid>
      )}
    </div>
  );
}
