import { Tooltip } from "@material-ui/core";
import AppBar from "@material-ui/core/AppBar";
import IconButton from "@material-ui/core/IconButton";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import MoreVertIcon from "@material-ui/icons/MoreVert";
import React from "react";
import Package from "../../package.json";

const Appbar = () => {
  const [options, setOptions] = React.useState(null);

  const openOptions = (event) => {
    setOptions(event.currentTarget);
  };

  const closeOptions = () => {
    setOptions(null);
  };

  const openMail = () => {
    window.open("mailto:mailtodephub@gmail.com");
  };

  const openApp = () => {
    window.open(
      "https://play.google.com/store/apps/details?id=com.dephub.android",
      "_self"
    );
  };

  const openFeedback = () => {
    window.open("https://gnaad.github.io/dephub/feedback.html", "_self");
  };

  return (
    <div>
      <AppBar position="sticky" elevation={0}>
        <Toolbar>
          <Typography variant="h6">
            DepHub Web
          </Typography>
          <Tooltip title="More Options">
            <IconButton
              aria-label="More Options"
              onClick={(event) => openOptions(event)}
              style={{
                marginLeft: "auto",
                marginRight: "0%",
              }}
            >
              <MoreVertIcon style={{ color: "white" }} label="More Options" />
            </IconButton>
          </Tooltip>
          <Menu
            id="simple-menu"
            anchorEl={options}
            keepMounted
            open={Boolean(options)}
            onClose={closeOptions}
          >
            <MenuItem onClick={() => openMail()}>Mail Us</MenuItem>
            <MenuItem onClick={() => openApp()}>Download Official App</MenuItem>
            <MenuItem onClick={() => openFeedback()}>Write Feedback</MenuItem>
            <MenuItem>Version - {Package.version}</MenuItem>
          </Menu>
        </Toolbar>
      </AppBar>
    </div>
  );
};

export default Appbar;
