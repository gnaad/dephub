import Button from "@material-ui/core/Button";
import CircularProgress from "@material-ui/core/CircularProgress";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";
import {
  OVERVIEW_TITLE,
  OVERVIEW_DESCRIPTION,
} from "../../constant/ApplicationConstant";
import "./OverviewDialog.css";

export default function Overview({
  data,
  overview,
  showOverview,
  closeOverview,
}) {
  return (
    <>
      <Dialog open={showOverview} onClose={closeOverview} fullWidth>
        <DialogTitle>{OVERVIEW_TITLE(data)}</DialogTitle>

        {overview.owner.type === "" ? (
          <DialogContent>
            <CircularProgress className="circular-progress" />
          </DialogContent>
        ) : (
          <DialogContent>
            <DialogContentText className="dialog-content-text">
              {OVERVIEW_DESCRIPTION(data, overview)}
            </DialogContentText>
          </DialogContent>
        )}

        <DialogActions>
          <Button onClick={closeOverview} color="primary" autoFocus>
            Close
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
