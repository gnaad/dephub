import { useState, useEffect } from "react";
import {
  Backdrop,
  CircularProgress,
  Grid,
  makeStyles,
} from "@material-ui/core";
import Dependency from "../layout/SingleCard/SingleCard";
import { API_URL } from "../constant/ApplicationConstant";

const customStyle = makeStyles((theme) => ({
  backdrop: {
    zIndex: theme.zIndex.drawer + 1,
    color: "#fff",
  },
}));

export default function SingleTab({ dependencyType }) {
  const style = customStyle();
  const [dependency, setDependency] = useState([]);

  useEffect(() => {
    fetch(API_URL)
      .then((result) => result.json())
      .then((response) => setDependency(response));
  }, []);

  return (
    <>
      {dependency.length === 0 ? (
        <Backdrop className={style.backdrop} open={true}>
          <CircularProgress color="primary" />
        </Backdrop>
      ) : (
        <Grid container spacing={2}>
          {dependency
            .sort((a, b) => (a.dependency_name > b.dependency_name ? 1 : -1))
            .filter((data) => data.type === dependencyType)
            .map((data) => (
              <Grid item xs={12} md={6} lg={4} key={data.id}>
                <Dependency data={data} />
              </Grid>
            ))}
        </Grid>
      )}
    </>
  );
}
