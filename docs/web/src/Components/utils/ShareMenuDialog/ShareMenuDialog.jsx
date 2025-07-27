import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";
import "./ShareMenuDialog.css";
import { DEPENDENCY_TEXT } from "../../constant/ApplicationConstant";

export default function ShareMenuDialog({ data, openShare, closeShare }) {
  return (
    <>
      <Dialog open={openShare} onClose={closeShare} fullWidth>
        <DialogTitle>Share Dependency</DialogTitle>
        <DialogContent>
          <DialogContentText className="dialog-content-text">
            {DEPENDENCY_TEXT(data)}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeShare} color="primary" autoFocus>
            Close
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
