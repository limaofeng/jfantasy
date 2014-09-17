<@override name="pageTitle">
<h3>
    Dashboard
    <small>
        Some examples to get you started
    </small>
</h3>
<div id="breadcrumb-right">
    <div id="sidebar-search">
        <input type="text" placeholder="Search..." class="autocomplete-input input tooltip-button" data-placement="bottom" title="Type &apos;jav&apos; to see the available tags..." id="" name="" />
        <i class="glyph-icon icon-search"></i>
    </div>
    <div class="float-right">
        <a href="javascript:;" class="btn medium bg-white tooltip-button black-modal-60 mrg5R" data-placement="bottom" title="Open modal window">
                                <span class="button-content">
                                    <i class="glyph-icon icon-question"></i>
                                </span>
        </a>

        <div class="dropdown">
            <a href="javascript:;" class="btn medium bg-white" title="Example dropdown" data-toggle="dropdown">
                                    <span class="button-content">
                                        <i class="glyph-icon icon-cog float-left"></i>
                                        <i class="glyph-icon icon-caret-down float-right"></i>
                                    </span>
            </a>
            <div class="dropdown-menu pad0A float-right">
                <div class="medium-box">
                    <div class="bg-gray text-transform-upr font-size-12 font-bold font-gray-dark pad10A">Form example</div>
                    <div class="pad10A">
                        <p class="font-gray-dark pad0B">This <span class="label bg-blue-alt">dropdown box</span> uses the Twitter Bootstrap dropdown plugin.</p>
                        <div class="divider mrg10T mrg10B"></div>

                        <form id="demo-form" action="" class="col-md-12" method="" />
                        <div class="form-row">
                            <div class="form-label col-md-4">
                                <label for="">
                                    Name:
                                    <span class="required">*</span>
                                </label>
                            </div>
                            <div class="form-input col-md-8">
                                <input type="text" id="email" name="email" data-type="email" data-trigger="change" data-required="true" class="parsley-validated" />
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-4">
                                <label for="">
                                    Email:
                                    <span class="required">*</span>
                                </label>
                            </div>
                            <div class="form-input col-md-8">
                                <input type="text" id="email" name="email" data-type="email" data-trigger="change" data-required="true" class="parsley-validated" />
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-4">
                                <label for="">
                                    Website:
                                </label>
                            </div>
                            <div class="form-input col-md-8">
                                <input type="text" id="website" name="website" data-trigger="change" data-type="url" class="parsley-validated" />
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-4">
                                <label for="" class="label-description">
                                    Message:
                                    <span>20 chars min, 200 max</span>
                                </label>
                            </div>
                            <div class="form-input col-md-8">
                                <textarea id="message" name="message" data-trigger="keyup" data-rangelength="[20,200]" class="parsley-validated"></textarea>
                            </div>
                        </div>
                        <div class="divider"></div>
                        <div class="form-row">
                            <input type="hidden" name="superhidden" id="superhidden" />
                            <div class="form-input col-md-8 col-md-offset-4">
                                <a href="javascript:;" class="btn medium primary-bg radius-all-4" id="demo-form-valid" onclick="javascript:$(&apos;#demo-form&apos;).parsley( &apos;validate&apos; );" title="Validate!">
                                                                <span class="button-content">
                                                                    Validate the form above
                                                                </span>
                                </a>
                            </div>
                        </div>

                        </form>

                    </div>

                    <div class="bg-black font-size-12 font-orange pad10A mrg5L mrg5R">Custom header example</div>
                    <div class="pad15A">
                        <p class="font-green text-center font-size-14 pad0B">Fides Admin comes with powerful helpers that you can use to create virtually any style you want. Read the documentation about helper classes to find out more!</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</@override>
<@override name="pageContent">

<div class="row">

<div class="col-md-6">

<div class="dashboard-panel bg-white content-box">
    <div class="content-box-wrapper">
        <div class="header text-left">
            Weekly forecast
            <span>November 2013 - December 2013</span>
        </div>
        <div class="center-div sparkline-bar-big-color-2"></div>
        <div class="nav-list-horizontal mrg25T">
            <ul class="row mrg10T">
                <li>
                    <div class="nav-wrp">
                        <h3><i class="glyph-icon icon-caret-up font-green font-size-17 pad5R"></i>$1486</h3>
                        <span class="font-gray">Total earnings</span>
                    </div>
                </li>
                <li>
                    <div class="nav-wrp">
                        <h3><i class="glyph-icon icon-minus font-yellow font-size-17 pad5R"></i>$863</h3>
                        <span class="font-gray">Weekly revenue</span>
                    </div>
                </li>
                <li>
                    <div class="nav-wrp">
                        <h3><i class="glyph-icon icon-caret-down font-red font-size-17 pad5R"></i>$622</h3>
                        <span class="font-gray">New sources</span>
                    </div>
                </li>
                <li>
                    <div class="nav-wrp">
                        <h3><i class="glyph-icon icon-caret-up font-green font-size-17 pad5R"></i>$65</h3>
                        <span class="font-gray">Gross margin</span>
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <div class="button-pane">
        <div class="medium float-left">
            <div class="form-input">
                <select data-placeholder="Change statistics area" class="chosen-select">
                    <option value="" />
                    <option value="United States" />United States
                    <option value="United Kingdom" />United Kingdom
                    <option value="Afghanistan" />Afghanistan
                    <option value="Aland Islands" />Aland Islands
                    <option value="Albania" />Albania
                    <option value="Algeria" />Algeria
                    <option value="American Samoa" />American Samoa
                    <option value="Andorra" />Andorra
                    <option value="Angola" />Angola
                    <option value="Anguilla" />Anguilla
                    <option value="Antarctica" />Antarctica
                    <option value="Antigua and Barbuda" />Antigua and Barbuda
                    <option value="Argentina" />Argentina
                    <option value="Armenia" />Armenia
                    <option value="Aruba" />Aruba
                    <option value="Australia" />Australia
                    <option value="Austria" />Austria
                    <option value="Azerbaijan" />Azerbaijan
                    <option value="Bahamas" />Bahamas
                    <option value="Bahrain" />Bahrain
                    <option value="Bangladesh" />Bangladesh
                    <option value="Barbados" />Barbados
                    <option value="Belarus" />Belarus
                    <option value="Belgium" />Belgium
                    <option value="Belize" />Belize
                    <option value="Benin" />Benin
                    <option value="Bermuda" />Bermuda
                    <option value="Bhutan" />Bhutan
                </select>
            </div>
        </div>

        <a href="javascript:;" class="medium btn bg-blue float-right tooltip-button" data-placement="top" title="More statistics">
            <i class="glyph-icon icon-plus"></i>
        </a>
    </div>
</div>

<div class="content-box">
    <table class="table text-center">
        <tbody>
        <tr>
            <td>1</td>
            <td class="font-bold text-left">John Clark</td>
            <td class="text-left"><a href="javascript:;" title="">Support</a></td>
            <td><div class="label bg-orange">+152</div></td>
            <td class="text-right">
                <div class="dropdown">
                    <a href="javascript:;" title="" class="btn medium bg-gray" data-toggle="dropdown">
                                    <span class="button-content">
                                        <i class="glyph-icon font-size-11 icon-cog"></i>
                                        <i class="glyph-icon font-size-11 icon-chevron-down"></i>
                                    </span>
                    </a>
                    <ul class="dropdown-menu float-right">

                        <li>
                            <a href="javascript:;" title="">
                                <i class="glyph-icon icon-edit mrg5R"></i>
                                Edit
                            </a>
                        </li>
                        <li>
                            <a href="javascript:;" title="">
                                <i class="glyph-icon icon-calendar mrg5R"></i>
                                Schedule
                            </a>
                        </li>
                        <li>
                            <a href="javascript:;" title="">
                                <i class="glyph-icon icon-download mrg5R"></i>
                                Download
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="javascript:;" class="font-red" title="">
                                <i class="glyph-icon icon-remove mrg5R"></i>
                                Delete
                            </a>
                        </li>
                    </ul>
                </div>
            </td>
        </tr>
        <tr>
            <td>2</td>
            <td class="font-bold text-left">Kenny Davis</td>
            <td class="text-left"><a href="javascript:;" title="">Business management</a></td>
            <td><div class="label bg-black">+152</div></td>
            <td class="text-right">
                <div class="dropdown">
                    <a href="javascript:;" title="" class="btn medium bg-gray" data-toggle="dropdown">
                                    <span class="button-content">
                                        <i class="glyph-icon font-size-11 icon-cog"></i>
                                        <i class="glyph-icon font-size-11 icon-chevron-down"></i>
                                    </span>
                    </a>
                    <ul class="dropdown-menu float-right">

                        <li>
                            <a href="javascript:;" title="">
                                <i class="glyph-icon icon-edit mrg5R"></i>
                                Edit
                            </a>
                        </li>
                        <li>
                            <a href="javascript:;" title="">
                                <i class="glyph-icon icon-calendar mrg5R"></i>
                                Schedule
                            </a>
                        </li>
                        <li>
                            <a href="javascript:;" title="">
                                <i class="glyph-icon icon-download mrg5R"></i>
                                Download
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="javascript:;" class="font-red" title="">
                                <i class="glyph-icon icon-remove mrg5R"></i>
                                Delete
                            </a>
                        </li>
                    </ul>
                </div>
            </td>
        </tr>
        <tr>
            <td>3</td>
            <td class="font-bold text-left">David Robertson</td>
            <td class="text-left"><a href="javascript:;" title="">Sales</a></td>
            <td><div class="label bg-green">+191</div></td>
            <td class="text-right">
                <div class="dropdown">
                    <a href="javascript:;" title="" class="btn medium bg-gray" data-toggle="dropdown">
                                    <span class="button-content">
                                        <i class="glyph-icon font-size-11 icon-cog"></i>
                                        <i class="glyph-icon font-size-11 icon-chevron-down"></i>
                                    </span>
                    </a>
                    <ul class="dropdown-menu float-right">

                        <li>
                            <a href="javascript:;" title="">
                                <i class="glyph-icon icon-edit mrg5R"></i>
                                Edit
                            </a>
                        </li>
                        <li>
                            <a href="javascript:;" title="">
                                <i class="glyph-icon icon-calendar mrg5R"></i>
                                Schedule
                            </a>
                        </li>
                        <li>
                            <a href="javascript:;" title="">
                                <i class="glyph-icon icon-download mrg5R"></i>
                                Download
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="javascript:;" class="font-red" title="">
                                <i class="glyph-icon icon-remove mrg5R"></i>
                                Delete
                            </a>
                        </li>
                    </ul>
                </div>
            </td>
        </tr>
        <tr>
            <td>4</td>
            <td class="font-bold text-left">John Doe</td>
            <td class="text-left"><a href="javascript:;" title="">Business</a></td>
            <td><div class="label bg-red">+483</div></td>
            <td class="text-right">
                <div class="dropdown">
                    <a href="javascript:;" title="" class="btn medium bg-gray" data-toggle="dropdown">
                                    <span class="button-content">
                                        <i class="glyph-icon font-size-11 icon-cog"></i>
                                        <i class="glyph-icon font-size-11 icon-chevron-down"></i>
                                    </span>
                    </a>
                    <ul class="dropdown-menu float-right">

                        <li>
                            <a href="javascript:;" title="">
                                <i class="glyph-icon icon-edit mrg5R"></i>
                                Edit
                            </a>
                        </li>
                        <li>
                            <a href="javascript:;" title="">
                                <i class="glyph-icon icon-calendar mrg5R"></i>
                                Schedule
                            </a>
                        </li>
                        <li>
                            <a href="javascript:;" title="">
                                <i class="glyph-icon icon-download mrg5R"></i>
                                Download
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="javascript:;" class="font-red" title="">
                                <i class="glyph-icon icon-remove mrg5R"></i>
                                Delete
                            </a>
                        </li>
                    </ul>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<div class="content-box bg-white post-box">
    <textarea name="" class="textarea-autoresize" id="" placeholder="What are you doing right now?"></textarea>
    <div class="button-pane">
        <a href="javascript:;" class="btn x-large hover-white" title="">
            <i class="glyph-icon icon-volume-down"></i>
        </a>
        <a href="javascript:;" class="btn x-large hover-white" title="">
            <i class="glyph-icon icon-facetime-video"></i>
        </a>
        <a href="javascript:;" class="btn x-large hover-white" title="">
            <i class="glyph-icon icon-microphone"></i>
        </a>
        <a href="javascript:;" class="btn x-large hover-white" title="">
            <i class="glyph-icon icon-picture"></i>
        </a>

        <a href="javascript:;" class="btn btn-post large bg-green" title="">
                    <span class="button-content">
                        Share it!
                    </span>
        </a>

    </div>
</div>

<div class="content-box remove-border text-center dashboard-buttons clearfix">
    <a href="javascript:;" class="btn vertical-button hover-blue-alt" title="">
                <span class="glyph-icon icon-separator-vertical pad0A medium">
                    <i class="glyph-icon icon-dashboard opacity-80 font-size-20"></i>
                </span>
        <span class="button-content">Dashboard</span>
    </a>
    <a href="javascript:;" class="btn vertical-button hover-green" title="">
                <span class="glyph-icon icon-separator-vertical pad0A medium">
                    <i class="glyph-icon icon-tags opacity-80 font-size-20"></i>
                </span>
        <span class="button-content">Widgets</span>
    </a>
    <a href="javascript:;" class="btn vertical-button hover-orange" title="">
                <span class="glyph-icon icon-separator-vertical pad0A medium">
                    <i class="glyph-icon icon-reorder opacity-80 font-size-20"></i>
                </span>
        <span class="button-content">Tables</span>
    </a>
    <a href="javascript:;" class="btn vertical-button hover-purple" title="">
                <span class="glyph-icon icon-separator-vertical pad0A medium">
                    <i class="glyph-icon icon-bar-chart opacity-80 font-size-20"></i>
                </span>
        <span class="button-content">Charts</span>
    </a>
    <a href="javascript:;" class="btn vertical-button hover-yellow" title="">
                <span class="glyph-icon icon-separator-vertical pad0A medium">
                    <i class="glyph-icon icon-laptop opacity-80 font-size-20"></i>
                </span>
        <span class="button-content">Buttons</span>
    </a>
    <a href="javascript:;" class="btn vertical-button hover-azure" title="">
                <span class="glyph-icon icon-separator-vertical pad0A medium">
                    <i class="glyph-icon icon-code opacity-80 font-size-20"></i>
                </span>
        <span class="button-content">Panels</span>
    </a>
    <a href="javascript:;" class="btn vertical-button hover-blue" title="">
                <span class="glyph-icon icon-separator-vertical pad0A medium">
                    <i class="glyph-icon icon-picture opacity-80 font-size-20"></i>
                </span>
        <span class="button-content">Themes</span>
    </a>
</div>

<div class="content-box box-toggle button-toggle">
    <h3 class="content-box-header primary-bg">
        <span class="float-left">Content Box</span>
        <a href="#" class="float-right icon-separator btn toggle-button" title="Toggle Box">
            <i class="glyph-icon icon-toggle icon-chevron-up"></i>
        </a>
        <a href="#" class="float-right icon-separator btn refresh-button" data-style="dark" data-theme="bg-white" data-opacity="60">
            <i class="glyph-icon icon-refresh"></i>
        </a>
        <span class="badge label btn bg-blue-alt font-size-11 mrg10R float-right">Label</span>
    </h3>
    <div class="content-box-wrapper">
        Content box with hidden header buttons.
    </div>
</div>

<div class="row">
    <div class="col-md-4">

        <div class="content-box">
            <h3 class="content-box-header ui-state-default">
                <span class="float-left">Remove box</span>
                <a href="#" class="float-right icon-separator btn tooltip-button remove-button" data-animation="flipOutY" data-placement="left" title="flipOutY Animation">
                    <i class="glyph-icon icon-remove"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
                Click the header remove button above to hide this content box.
            </div>
        </div>

    </div>
    <div class="col-md-4">

        <div class="content-box">
            <h3 class="content-box-header ui-state-default">
                <span class="float-left">Remove box</span>
                <a href="#" class="float-right icon-separator btn tooltip-button remove-button" data-animation="fadeOutLeft" data-placement="left" title="fadeOutLeft Animation">
                    <i class="glyph-icon icon-remove"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
                Click the header remove button above to hide this content box.
            </div>
        </div>

    </div>
    <div class="col-md-4">

        <div class="content-box">
            <h3 class="content-box-header ui-state-default">
                <span class="float-left">Remove box</span>
                <a href="#" class="float-right icon-separator btn tooltip-button remove-button" data-animation="fadeOutRight" data-placement="left" title="fadeOutRight Animation">
                    <i class="glyph-icon icon-remove"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
                Click the header remove button above to hide this content box.
            </div>
        </div>

    </div>
</div>

</div>
<div class="col-md-6">

<div class="row">
    <div class="col-lg-6">

        <div class="dashboard-panel content-box remove-border bg-blue-alt">
            <div class="content-box-wrapper">
                <div class="header">
                    New registrations
                    <span>August - October 2013</span>
                </div>
                <div class="sparkline-big">183,579,180,311,405,342,579,405,311,311,450,302,370,510</div>
            </div>
            <div class="button-pane">
                <div class="heading medium">
                    <b>521</b> active accounts
                </div>
                <a href="javascript:;" class="medium btn bg-blue float-right tooltip-button" data-placement="top" title="Download now!">
                    <i class="glyph-icon icon-cloud-download"></i>
                </a>
            </div>
        </div>

        <div class="dashboard-panel content-box remove-border bg-orange">
            <div class="content-box-wrapper">
                <div class="header">
                    Shipped items
                    <span>January - December 2013</span>
                </div>
                <div class="sparkline-big">183,579,180,311,405,342,579,405,450,311,180,311,450,302,370,210</div>
            </div>
            <div class="button-pane">
                <div class="heading medium">
                    <b>5</b> pending shipments
                </div>
                <a href="javascript:;" class="medium btn bg-black float-right tooltip-button" data-placement="top" title="View details">
                    <i class="glyph-icon icon-cog"></i>
                </a>
            </div>
        </div>

    </div>
    <div class="col-lg-6">

        <div class="dashboard-panel content-box remove-border bg-green">
            <div class="content-box-wrapper">
                <div class="header">
                    YoY growth
                    <span>2013 - 2014</span>
                </div>
                <div class="sparkline-big">183,579,180,311,240,180,311,450,302,370,210,610</div>
            </div>
            <div class="button-pane">
                <div class="heading medium">
                    <b>289</b> new visits
                </div>
                <a href="javascript:;" class="medium btn bg-yellow float-right tooltip-button" data-placement="top" title="Add content">
                    <i class="glyph-icon icon-plus"></i>
                </a>
            </div>
        </div>

        <div class="dashboard-panel content-box remove-border bg-yellow">
            <div class="content-box-wrapper">
                <div class="header">
                    Monthly evolution
                    <span>July - December 2013</span>
                </div>
                <div class="sparkline-big">183,579,180,311,230,311,405,342,230,302,405,230,405,342,579,405</div>
            </div>
            <div class="button-pane">
                <div class="heading medium">
                    <b>8</b> overdue orders
                </div>
                <a href="javascript:;" class="medium btn bg-white float-right tooltip-button" data-placement="top" title="View details">
                    <i class="glyph-icon icon-link"></i>
                </a>
            </div>
        </div>

    </div>
</div>
<div class="content-box nav-list-horizontal">
    <ul class="row">
        <li>
            <a href="javascript:;" title="">
                <i class="glyph-icon font-purple icon-dashboard"></i>
                Dashboard
            </a>
        </li>
        <li>
            <a href="javascript:;" title="">
                <i class="glyph-icon font-blue-alt icon-map-marker"></i>
                Location
            </a>
        </li>
        <li>
            <a href="javascript:;" title="">
                <i class="glyph-icon font-green icon-bullhorn"></i>
                Announcements
            </a>
        </li>
        <li>
            <a href="javascript:;" title="">
                <i class="glyph-icon font-red icon-camera"></i>
                Photo Gallery
            </a>
        </li>
    </ul>
</div>

<div class="row">
    <div class="col-lg-6">

        <div class="profile-box profile-box-alt content-box">
            <div class="content-box-header clearfix bg-blue">
                <div class="user-details">
                    There have been <b>134</b> sales
                    <span>January 2013 - December 2014</span>
                </div>
            </div>

            <div class="pie-wrapper">
                <div class="pie-sparkline-alt">5,2,3</div>
            </div>

            <table class="table">
                <tbody>
                <tr>
                    <td class="font-bold text-left">
                        <div class="badge badge-small bg-blue"></div>
                        New user registrations
                    </td>
                    <td class="text-center">
                        <a href="javascript:;" class="btn small hover-yellow tooltip-button" data-placement="top" title="Flag">
                            <i class="glyph-icon icon-flag"></i>
                        </a>
                        <a href="javascript:;" class="btn small hover-blue-alt tooltip-button" data-placement="top" title="Edit">
                            <i class="glyph-icon icon-edit"></i>
                        </a>
                        <a href="javascript:;" class="btn small hover-red tooltip-button" data-placement="top" title="Remove">
                            <i class="glyph-icon icon-remove"></i>
                        </a>
                    </td>
                </tr>
                <tr>
                    <td class="font-bold text-left">
                        <div class="badge badge-small bg-orange"></div>
                        Returning visitors
                    </td>
                    <td class="text-center">
                        <a href="javascript:;" class="btn small hover-yellow tooltip-button" data-placement="top" title="Flag">
                            <i class="glyph-icon icon-flag"></i>
                        </a>
                        <a href="javascript:;" class="btn small hover-blue-alt tooltip-button" data-placement="top" title="Edit">
                            <i class="glyph-icon icon-edit"></i>
                        </a>
                        <a href="javascript:;" class="btn small hover-red tooltip-button" data-placement="top" title="Remove">
                            <i class="glyph-icon icon-remove"></i>
                        </a>
                    </td>
                </tr>
                <tr>
                    <td class="font-bold text-left">
                        <div class="badge badge-small bg-gray"></div>
                        Page views
                    </td>
                    <td class="text-center">
                        <a href="javascript:;" class="btn small hover-yellow tooltip-button" data-placement="top" title="Flag">
                            <i class="glyph-icon icon-flag"></i>
                        </a>
                        <a href="javascript:;" class="btn small hover-blue-alt tooltip-button" data-placement="top" title="Edit">
                            <i class="glyph-icon icon-edit"></i>
                        </a>
                        <a href="javascript:;" class="btn small hover-red tooltip-button" data-placement="top" title="Remove">
                            <i class="glyph-icon icon-remove"></i>
                        </a>
                    </td>
                </tr>
                </tbody>
            </table>

        </div>

    </div>
    <div class="col-lg-6">
        <div class="profile-box content-box">
            <div class="content-box-header clearfix ui-state-default">
                <img width="36" src="assets/images/gravatar.jpg" alt="" />
                <div class="user-details">
                    Horia Simon
                    <span>User Interface Designer</span>
                </div>
            </div>
            <div class="nav-list">
                <ul>
                    <li>
                        <a href="javascript:;" title="">
                            <i class="glyph-icon font-purple icon-dashboard"></i>
                            Dashboard
                            <i class="glyph-icon icon-chevron-right float-right"></i>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:;" title="">
                            <i class="glyph-icon font-orange icon-map-marker"></i>
                            Location
                            <i class="glyph-icon icon-chevron-right float-right"></i>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:;" title="">
                            <i class="glyph-icon font-green icon-bullhorn"></i>
                            Announcements
                            <i class="glyph-icon icon-chevron-right float-right"></i>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:;" title="">
                            <i class="glyph-icon font-red icon-camera"></i>
                            Photo Gallery
                            <i class="glyph-icon icon-chevron-right float-right"></i>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="profile-box bg-white remove-border content-box">
            <div class="content-box-header clearfix">
                <img width="36" src="assets/images/gravatar.jpg" alt="" />
                <div class="user-details">
                    Horia Simon
                    <span>User Interface Designer</span>
                </div>
                <div class="pad10T">
                    <div class="row">
                        <div class="col-xs-6">
                            <a href="javascript:;" class="btn display-block small bg-facebook">
                                        <span class="glyph-icon icon-separator">
                                            <i class="glyph-icon icon-facebook"></i>
                                        </span>
                                        <span class="button-content">
                                            Facebook
                                        </span>
                            </a>
                        </div>
                        <div class="col-xs-6">
                            <a href="javascript:;" class="btn display-block small bg-twitter">
                                        <span class="glyph-icon icon-separator">
                                            <i class="glyph-icon icon-twitter"></i>
                                        </span>
                                        <span class="button-content">
                                            Twitter
                                        </span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</div>

</div>

</@override>
<@extends name="wrapper.ftl"/>