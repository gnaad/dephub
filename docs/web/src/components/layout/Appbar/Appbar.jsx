import React from "react";
import AppBar from "@material-ui/core/AppBar";
import IconButton from "@material-ui/core/IconButton";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import MoreVertIcon from "@material-ui/icons/MoreVert";
import Toolbar from "@material-ui/core/Toolbar";
import Tooltip from "@material-ui/core/Tooltip";
import Typography from "@material-ui/core/Typography";
import Package from "../../../../package.json";
import {
  EMAIL_ADDRESS,
  FEEDBACK_URL,
  PLAYSTORE_URL,
} from "../../constant/ApplicationConstant";
import "./Appbar.css";

const Appbar = () => {
  const [menuOption, setMenuOption] = React.useState(null);

  const openMenuOption = (event) => {
    setMenuOption(event.currentTarget);
  };

  const closeMenuOption = () => {
    setMenuOption(null);
  };

  const openEmailClient = () => {
    window.open(EMAIL_ADDRESS);
  };

  const openPlaystore = () => {
    window.open(PLAYSTORE_URL, "_self");
  };

  const openFeedbackForm = () => {
    window.open(FEEDBACK_URL, "_self");
  };

  return (
    <>
      <AppBar position="sticky" elevation={0}>
        <Toolbar>
          <Typography variant="h6">DepHub Web</Typography>

          <Tooltip title="More Options">
            <IconButton
              aria-label="More Options"
              style={{ marginLeft: "auto" }}
              onClick={(event) => openMenuOption(event)}
            >
              <MoreVertIcon className="more-vert-icon" />
            </IconButton>
          </Tooltip>

          <Menu
            keepMounted
            open={Boolean(menuOption)}
            anchorEl={menuOption}
            onClose={closeMenuOption}
          >
            <MenuItem onClick={() => openEmailClient()}>Mail Us</MenuItem>
            <MenuItem onClick={() => openPlaystore()}>
              Download Official App
            </MenuItem>
            <MenuItem onClick={() => openFeedbackForm()}>
              Write Feedback
            </MenuItem>
            <MenuItem>Version - {Package.version}</MenuItem>
          </Menu>
        </Toolbar>
      </AppBar>
    </>
  );
};

export default Appbar;
